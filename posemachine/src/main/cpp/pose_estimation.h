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

/* Header for class com_xiaomi_mace_JniMaceUtils */

#ifndef CPP_POSE_ESTIMATION_H_
#define CPP_POSE_ESTIMATION_H_

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_xiaomi_mace_JniMaceUtils
 * Method:    macePersonlabCreateGPUContext
 * Signature: (Ljava/lang/String;IIIILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_com_xiaomi_mace_JniMaceUtils_macePersonlabCreateGPUContext(JNIEnv *,
                                                                jclass,
                                                                jstring);

/*
 * Class:     com_xiaomi_mace_JniMaceUtils
 * Method:    macePersonlabCreateEngine
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_com_xiaomi_mace_JniMaceUtils_macePersonlabCreateEngine
  (JNIEnv *, jclass, jint, jint, jint, jint, jstring, jstring);

/*
 * Class:     com_xiaomi_mace_JniMaceUtils
 * Method:    macePersonlabEstimate
 * Signature: ([F)[F
 */
JNIEXPORT jint JNICALL
Java_com_xiaomi_mace_JniMaceUtils_macePersonlabEstimate
  (JNIEnv *, jclass, jfloatArray, jfloatArray, jfloatArray, jfloatArray);

#ifdef __cplusplus
}
#endif
#endif  // CPP_POSE_ESTIMATION_H_
