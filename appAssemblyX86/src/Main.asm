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
  
  ;getting X, Y to display window in center
  mov eax, [screenWidth]
	sub eax, [windowWidth]
	sar eax, 1 ;divide on 2     
	mov [windowX], eax

	mov eax, [screenHeight]
	sub eax, [windowHeight]
	sar eax, 1 ;divide on 2
	mov [windowY], eax

  invoke CreateWindowEx, WS_EX_CLIENTEDGE, className, windowTitle, WS_VISIBLE + WS_OVERLAPPEDWINDOW, [windowX], [windowY], [windowWidth], [windowHeight], NULL, NULL, NULL, NULL
 
msg_loop:
  invoke GetMessage, msg, NULL, 0, 0
  cmp eax, 0
  je end_loop
  invoke TranslateMessage, msg
  invoke DispatchMessage, msg
  jmp msg_loop

end_loop:
  invoke ExitProcess, [msg.wParam]

proc WindowProc uses ebx esi edi, hwnd, wmsg, wparam, lparam
  cmp [wmsg], WM_CREATE
  je .wmcreate
  cmp [wmsg], WM_COMMAND
  je .wmcommand
  cmp [wmsg], WM_GETMINMAXINFO
  je .wmgetminmaxinfo
  cmp [wmsg], WM_CLOSE
  je .wmclose
  cmp [wmsg], WM_DESTROY
  je .wmdestroy
  
.defwndproc:
  invoke DefWindowProc, [hwnd], [wmsg], [wparam], [lparam]
  jmp .finish

.wmcreate:  
  ; TODO   
  jmp .finish
      
.wmcommand:
  ; TODO   
  jmp .finish
  
.wmgetminmaxinfo:
  ; TODO   
  jmp .finish

.wmclose:  
  invoke DestroyWindow, [hwnd]
  jmp .finish
  
.wmdestroy:
  invoke PostQuitMessage, 0
  xor eax, eax
  
.finish:
  ret
endp

section '.data' data readable writeable

  className TCHAR 'HummelCalculator', 0
  windowTitle TCHAR 'WinAPI', 0
  
	screenWidth dd 0
	screenHeight dd 0
	windowWidth dd 260
	windowHeight dd 453
	windowX dd 0
	windowY dd 0

  wc WNDCLASS 0, WindowProc, 0, 0, NULL, NULL, NULL, COLOR_WINDOW + 1, NULL, className

  msg MSG

section '.idata' import data readable writeable

  library kernel32, 'KERNEL32.DLL', user32, 'USER32.DLL'

  include 'api\kernel32.inc'
  include 'api\user32.inc'