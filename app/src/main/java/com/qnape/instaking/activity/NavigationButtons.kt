package com.qnape.instaking.activity

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.qnape.instaking.R

class NavigationButtons(context: Context, attrs: AttributeSet?) :
    BottomNavigationView(context, attrs) {

    private val FAB_RADIUS = resources.getDimension(R.dimen.fab_size) / 2
    // offset of the first control point (top part)
    private val TOP_CONTROL_X = FAB_RADIUS + FAB_RADIUS / 2
    private val TOP_CONTROL_Y = FAB_RADIUS / 6
    // offset of the second control point (bottom part)
    private val BOTTOM_CONTROL_X = FAB_RADIUS + (FAB_RADIUS / 2)
    private val BOTTOM_CONTROL_Y = FAB_RADIUS / 4
    // width of the curve
    private val CURVE_OFFSET = FAB_RADIUS * 2 + (FAB_RADIUS / 6)
    private val TAG = "CurvedBottomNavigation"

    // first bezier curve
    private val firstCurveStart = PointF()
    private val firstCurveEnd = PointF()
    private val firstCurveControlPoint1 = PointF()
    private val firstCurveControlPoint2 = PointF()

    // second bezier curve
    private val secondCurveStart = PointF()
    private val secondCurveEnd = PointF()
    private val secondCurveControlPoint1 = PointF()
    private val secondCurveControlPoint2 = PointF()

    // path to represent the background including the curve
    private val path: Path = Path()
    // just a basic paint
    private val paint: Paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
    }

    init {
        // Important, we will be drawing our own background
        setBackgroundColor(Color.TRANSPARENT)
        labelVisibilityMode = LABEL_VISIBILITY_UNLABELED
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // first curve
        // set the starting point of the curve (P2)
        firstCurveStart.apply {
            // we want the curve to start at CURVE_OFFSET before the center of the view
            x = (w / 2) - CURVE_OFFSET
            y = 0f
        }
        // set the end point for the first curve (P3)
        firstCurveEnd.apply {
            x = (w / 2f)
            y = FAB_RADIUS + (FAB_RADIUS / 4)
        }
        // set the first control point (C1)
        firstCurveControlPoint1.apply {
            x = firstCurveStart.x + TOP_CONTROL_X
            y = TOP_CONTROL_Y
        }
        // set the second control point (C2)
        firstCurveControlPoint2.apply {
            x = firstCurveEnd.x - BOTTOM_CONTROL_X
            y = firstCurveEnd.y - BOTTOM_CONTROL_Y
        }

        // second curve
        // end of first curve and start of second curve is the same (P3)
        secondCurveStart.set(firstCurveEnd.x, firstCurveEnd.y)
        // end of the second curve (P4)
        secondCurveEnd.apply {
            x = (w / 2) + CURVE_OFFSET
            y = 0f
        }
        // set the first control point of second curve (C4)
        secondCurveControlPoint1.apply {
            x = secondCurveStart.x + BOTTOM_CONTROL_X
            y = secondCurveStart.y - BOTTOM_CONTROL_Y
        }
        // set the second control point (C3)
        secondCurveControlPoint2.apply {
            x = secondCurveEnd.x - TOP_CONTROL_X
            y = TOP_CONTROL_Y
        }

        // clear any previous path
        path.reset()
        // start from P1 of the BottomNavigationView
        path.moveTo(0f, 0f)
        // horizontal line from P1 to P2
        path.lineTo(firstCurveStart.x, firstCurveStart.y)
        // bezier curve with (P2, C1, C2, P3)
        path.cubicTo(
            firstCurveControlPoint1.x,
            firstCurveControlPoint1.y,
            firstCurveControlPoint2.x,
            firstCurveControlPoint2.y,
            firstCurveEnd.x,
            firstCurveEnd.y
        )
        // bezier curve with (P3, C4, C3, P4)
        path.cubicTo(
            secondCurveControlPoint1.x,
            secondCurveControlPoint1.y,
            secondCurveControlPoint2.x,
            secondCurveControlPoint2.y,
            secondCurveEnd.x,
            secondCurveEnd.y
        )
        // line from P4 to P5
        path.lineTo(w.toFloat(), 0f)
        // line from P5 to P6
        path.lineTo(w.toFloat(), h.toFloat())
        // line from P6 to P7
        path.lineTo(0f, h.toFloat())
        // complete the path
        path.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }
}