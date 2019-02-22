package com.kakaovx.homet.user.component.ui.module

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.model.layoutmanager.SpanningLinearLayoutManager

class HorizontalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false)
class VerticalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)