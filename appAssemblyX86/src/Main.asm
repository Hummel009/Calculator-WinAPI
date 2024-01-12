format PE GUI 4.0
entry Start

include 'win32a.inc'

section '.text' code readable executable

Start:
  invoke RegisterClass, wc                            
 
  invoke GetSystemMetrics, SM_CXSCREEN
	mov [screenWidth], eax

	invoke GetSystemMetrics, SM_CYSCREEN
	mov [screenHeight], eax
  
  mov eax, [screenWidth]
	sub eax, [windowWidth]
	sar eax, 1 ;divide on 2     
	mov [windowX], eax

	mov eax, [screenHeight]
	sub eax, [windowHeight]
	sar eax, 1 ;divide on 2
	mov [windowY], eax

  invoke CreateWindowEx, 0, _class, _title, WS_VISIBLE + WS_DLGFRAME + WS_SYSMENU, [windowX], [windowY], [windowWidth], [windowHeight], NULL, NULL, [wc.hInstance], NULL
 
msg_loop:
  invoke GetMessage, msg, NULL, 0, 0
  cmp eax, 1
  jb end_loop
  jne msg_loop
  invoke TranslateMessage, msg
  invoke DispatchMessage, msg
  jmp msg_loop

end_loop:
  invoke ExitProcess, [msg.wParam]

proc WindowProc uses ebx esi edi, hwnd, wmsg, wparam, lparam
  cmp [wmsg], WM_DESTROY
  je .wmdestroy
  
.defwndproc:
  invoke DefWindowProc, [hwnd], [wmsg], [wparam], [lparam]
  jmp .finish
  
.wmdestroy:
  invoke PostQuitMessage,0
  xor eax, eax
  
.finish:
  ret
endp

section '.data' data readable writeable

  _class TCHAR 'HummelCalculator', 0
  _title TCHAR 'WinAPI', 0
  _error TCHAR 'Startup failed.', 0  
  
	screenWidth dd 0
	screenHeight dd 0
	windowWidth dd 260
	windowHeight dd 453
	windowX dd 0
	windowY dd 0

  wc WNDCLASS 0, WindowProc, 0, 0, NULL, NULL, NULL, COLOR_BTNFACE + 1, NULL, _class

  msg MSG

section '.idata' import data readable writeable

  library kernel32, 'KERNEL32.DLL', user32, 'USER32.DLL'

  include 'api\kernel32.inc'
  include 'api\user32.inc'