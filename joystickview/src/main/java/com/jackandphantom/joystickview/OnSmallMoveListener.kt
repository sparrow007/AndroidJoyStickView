package com.jackandphantom.joystickview

/*
 * Interface called when InnerCircleView or joystick point is moving and this interface will provide the
 * angle and strength
*/
internal interface OnSmallMoveListener {
    fun onMove(angle: Double, strength: Float)
}
