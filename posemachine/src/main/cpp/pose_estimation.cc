// Copyright 2018 The MACE Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#include "pose_estimation.h"

#include <android/log.h>
#include <jni.h>

#include <algorithm>
#include <functional>
#include <map>
#include <memory>
#include <string>
#include <vector>
#include <numeric>

#include "mace/public/mace.h"
#include "mace/public/mace_engine_factory.h"

namespace {

struct ModelInfo {
  std::string input_name;
  std::vector<std::string> output_names;
  std::vector<int64_t> input_shape;
  std::vector<std::vector<int64_t>> output_shapes;
};

struct MaceContext {
  std::shared_ptr<mace::GPUContext> gpu_context;
  std::shared_ptr<mace::MaceEngine> engine;
  std::string model_name;
  mace::DeviceType device_type = mace::DeviceType::CPU;
  std::map<std::string, ModelInfo> model_infos = {
      {"personlab", {"MobilenetV1/input_1", {"tower0/Top/kp_maps/Sigmoid", "tower0/Top/short_offsets/BiasAdd", "tower0/Top/mid_offsets/BiasAdd"},
                            {1, 224, 224, 3}, {{1, 14, 14, 17}, {1, 14, 14, 34}, {1, 14, 14, 72}}}},
  };
};

mace::DeviceType ParseDeviceType(const std::string &device) {
  if (device.compare("CPU") == 0) {
    return mace::DeviceType::CPU;
  } else if (device.compare("GPU") == 0) {
    return mace::DeviceType::GPU;
  } else if (device.compare("HEXAGON") == 0) {
    return mace::DeviceType::HEXAGON;
  } else {
    return mace::DeviceType::CPU;
  }
}

MaceContext& GetMaceContext() {
  // TODO(yejianwu): In multi-dlopen process, this step may cause memory leak.
  static auto *mace_context = new MaceContext;

  return *mace_context;
}

}  // namespace

JNIEXPORT jint JNICALL
Java_com_xiaomi_mace_JniMaceUtils_macePersonlabCreateGPUContext(
    JNIEnv *env, jclass thisObj, jstring storage_path) {
  MaceContext &mace_context = GetMaceContext();
  // DO NOT USE tmp directory.
  // Please use APP's own directory and make sure the directory exists.
  const char *storage_path_ptr = env->GetStringUTFChars(storage_path, nullptr);
  if (storage_path_ptr == nullptr) return JNI_ERR;
  const std::string storage_file_path(storage_path_ptr);
  env->ReleaseStringUTFChars(storage_path, storage_path_ptr);

  mace_context.gpu_context = mace::GPUContextBuilder()
      .SetStoragePath(storage_file_path)
      .Finalize();

  return JNI_OK;
}

JNIEXPORT jint JNICALL
Java_com_xiaomi_mace_JniMaceUtils_macePersonlabCreateEngine(
    JNIEnv *env, jclass thisObj, jint omp_num_threads, jint cpu_affinity_policy,
    jint gpu_perf_hint, jint gpu_priority_hint,
    jstring model_name_str, jstring device) {
  MaceContext &mace_context = GetMaceContext();

  // get device
  const char *device_ptr = env->GetStringUTFChars(device, nullptr);
  if (device_ptr == nullptr) return JNI_ERR;
  mace_context.device_type = ParseDeviceType(device_ptr);
  env->ReleaseStringUTFChars(device, device_ptr);

  // create MaceEngineConfig
  mace::MaceStatus status;
  mace::MaceEngineConfig config(mace_context.device_type);
  status = config.SetCPUThreadPolicy(
      omp_num_threads,
      static_cast<mace::CPUAffinityPolicy>(cpu_affinity_policy),
      true);
  if (status != mace::MaceStatus::MACE_SUCCESS) {
    __android_log_print(ANDROID_LOG_ERROR,
                        "VX_pose_estimation attrs",
                        "openmp result: %s, threads: %d, cpu: %d",
                        status.information().c_str(), omp_num_threads,
                        cpu_affinity_policy);
  }
  if (mace_context.device_type == mace::DeviceType::GPU) {
    config.SetGPUContext(mace_context.gpu_context);
    config.SetGPUHints(
        static_cast<mace::GPUPerfHint>(gpu_perf_hint),
        static_cast<mace::GPUPriorityHint>(gpu_priority_hint));
    __android_log_print(ANDROID_LOG_INFO,
                        "VX_pose_estimation attrs",
                        "gpu perf: %d, priority: %d",
                        gpu_perf_hint, gpu_priority_hint);
  }

  __android_log_print(ANDROID_LOG_INFO,
                      "VX_pose_estimation attrs",
                      "device: %d",
                      mace_context.device_type);

  //  parse model name
  const char *model_name_ptr = env->GetStringUTFChars(model_name_str, nullptr);
  if (model_name_ptr == nullptr) return JNI_ERR;
  mace_context.model_name.assign(model_name_ptr);
  env->ReleaseStringUTFChars(model_name_str, model_name_ptr);

  //  load model input and output name
  auto model_info_iter =
      mace_context.model_infos.find(mace_context.model_name);
  if (model_info_iter == mace_context.model_infos.end()) {
    __android_log_print(ANDROID_LOG_ERROR,
                        "VX_pose_estimation",
                        "Invalid model name: %s",
                        mace_context.model_name.c_str());
    return JNI_ERR;
  }
  std::vector<std::string> input_names = {model_info_iter->second.input_name};
  std::vector<std::string> output_names = model_info_iter->second.output_names;

  mace::MaceStatus create_engine_status =
      CreateMaceEngineFromCode(mace_context.model_name,
                               std::string(),
                               input_names,
                               output_names,
                               config,
                               &mace_context.engine);

  __android_log_print(ANDROID_LOG_INFO,
                      "VX_pose_estimation attrs",
                      "create result: %s",
                      create_engine_status.information().c_str());

  return create_engine_status == mace::MaceStatus::MACE_SUCCESS ?
         JNI_OK : JNI_ERR;
}

//JNIEXPORT jfloatArray JNICALL
JNIEXPORT jint JNICALL
Java_com_xiaomi_mace_JniMaceUtils_macePersonlabEstimate(
    JNIEnv *env, jclass thisObj, jfloatArray input_data, jfloatArray output_kp_maps, jfloatArray output_short_offsets, jfloatArray output_mid_offsets) {
  MaceContext &mace_context = GetMaceContext();
  //  prepare input and output
  auto model_info_iter =
      mace_context.model_infos.find(mace_context.model_name);
  if (model_info_iter == mace_context.model_infos.end()) {
    __android_log_print(ANDROID_LOG_ERROR,
                        "VX_pose_estimation",
                        "Invalid model name: %s",
                        mace_context.model_name.c_str());
    //return nullptr;
    return JNI_ERR;
  }
  const ModelInfo &model_info = model_info_iter->second;
  const std::string &input_name = model_info.input_name;
  const std::vector<std::string> &output_names = model_info.output_names;
  const std::vector<int64_t> &input_shape = model_info.input_shape;
  const std::vector<std::vector<int64_t>> &output_shapes = model_info.output_shapes;
  const int64_t input_size =
      std::accumulate(input_shape.begin(), input_shape.end(), 1,
                      std::multiplies<int64_t>());
  const int64_t output_size_kp_maps =
      std::accumulate(output_shapes[0].begin(), output_shapes[0].end(), 1,
                      std::multiplies<int64_t>());
  const int64_t output_size_short_offsets =
      std::accumulate(output_shapes[1].begin(), output_shapes[1].end(), 1,
                      std::multiplies<int64_t>());
  const int64_t output_size_mid_offsets =
      std::accumulate(output_shapes[2].begin(), output_shapes[2].end(), 1,
                      std::multiplies<int64_t>());

  //  load input
  jfloat *input_data_ptr = env->GetFloatArrayElements(input_data, nullptr);
  if (input_data_ptr == nullptr) {
    __android_log_print(ANDROID_LOG_ERROR,
                      "VX_pose_estimation",
                      "Invalid input ptr");
    return JNI_ERR; //return nullptr;
  }
  jsize length = env->GetArrayLength(input_data);
  if (length != input_size) {
    __android_log_print(ANDROID_LOG_ERROR,
                      "VX_pose_estimation",
                      "Invalid length");
    return JNI_ERR; //return nullptr;
  }

  std::map<std::string, mace::MaceTensor> inputs;
  std::map<std::string, mace::MaceTensor> outputs;
  // construct input
  auto buffer_in = std::shared_ptr<float>(new float[input_size],
                                          std::default_delete<float[]>());
  std::copy_n(input_data_ptr, input_size, buffer_in.get());
  env->ReleaseFloatArrayElements(input_data, input_data_ptr, 0);
  inputs[input_name] = mace::MaceTensor(input_shape, buffer_in);

  // construct output
  auto buffer_out_kp_maps = std::shared_ptr<float>(new float[output_size_kp_maps],
                                           std::default_delete<float[]>());
  auto buffer_out_short_offsets = std::shared_ptr<float>(new float[output_size_short_offsets],
                                           std::default_delete<float[]>());
  auto buffer_out_mid_offsets = std::shared_ptr<float>(new float[output_size_mid_offsets],
                                           std::default_delete<float[]>());
  outputs[output_names[0]] = mace::MaceTensor(output_shapes[0], buffer_out_kp_maps);
  outputs[output_names[1]] = mace::MaceTensor(output_shapes[1], buffer_out_short_offsets);
  outputs[output_names[2]] = mace::MaceTensor(output_shapes[2], buffer_out_mid_offsets);

  // run model
  mace_context.engine->Run(inputs, &outputs);

  // transform output
  env->SetFloatArrayRegion(output_kp_maps, 0, output_size_kp_maps, outputs[output_names[0]].data().get());
  env->SetFloatArrayRegion(output_short_offsets, 0, output_size_short_offsets, outputs[output_names[1]].data().get());
  env->SetFloatArrayRegion(output_mid_offsets, 0, output_size_mid_offsets, outputs[output_names[2]].data().get());

  /*
  // transform output
  jfloatArray jOutputData = env->NewFloatArray(output_size_kp_maps + output_size_short_offsets + output_size_mid_offsets);  // allocate
  if (jOutputData == nullptr) return nullptr;
  env->SetFloatArrayRegion(jOutputData, 0, output_size_kp_maps,
                           outputs[output_names[0]].data().get());  // copy

  env->SetFloatArrayRegion(jOutputData, output_size_kp_maps, output_size_short_offsets,
                           outputs[output_names[1]].data().get());

  env->SetFloatArrayRegion(jOutputData, output_size_kp_maps + output_size_short_offsets, output_size_mid_offsets,
                           outputs[output_names[2]].data().get());

  return jOutputData;
  */
  return JNI_OK;
}
