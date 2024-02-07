package com.github.hummel.wincalc;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APIOptions;

@SuppressWarnings("FunctionName")
interface ExUser32 extends User32 {
	ExUser32 INSTANCE = Native.load("user32", ExUser32.class, W32APIOptions.DEFAULT_OPTIONS);

	boolean SetWindowText(HWND hWnd, String lpString);
}