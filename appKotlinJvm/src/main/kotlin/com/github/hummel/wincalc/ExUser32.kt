package com.github.hummel.wincalc

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.win32.W32APIOptions

@Suppress("FunctionName")
internal interface ExUser32 : User32 {
	companion object {
		val INSTANCE: ExUser32 = Native.load("user32", ExUser32::class.java, W32APIOptions.DEFAULT_OPTIONS)
	}

	fun SetWindowText(hWnd: HWND?, lpString: String): Boolean
}