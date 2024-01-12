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

proc WindowProc uses ebx esi edi, window, msg, wParam, lParam
  cmp [msg], WM_CREATE
  je .wmcreate
  cmp [msg], WM_COMMAND
  je .wmcommand
  cmp [msg], WM_GETMINMAXINFO
  je .wmgetminmaxinfo
  cmp [msg], WM_CLOSE
  je .wmclose
  cmp [msg], WM_DESTROY
  je .wmdestroy
  
.defwndproc:
  invoke DefWindowProc, [window], [msg], [wParam], [lParam]
  jmp .finish

.wmcreate:
  stdcall RegisterButton, [window], 0, btnPi, 0, 60 * 1 - 10  
  stdcall RegisterButton, [window], 1, btnE, 60, 60 * 1 - 10  
  stdcall RegisterButton, [window], 2, btnC, 120, 60 * 1 - 10  
  stdcall RegisterButton, [window], 3, btnFact, 180, 60 * 1 - 10
  stdcall RegisterButton, [window], 4, btnInv, 0, 60 * 2 - 10  
  stdcall RegisterButton, [window], 5, btnSq, 60, 60 * 2 - 10  
  stdcall RegisterButton, [window], 6, btnSqrt, 120, 60 * 2 - 10  
  stdcall RegisterButton, [window], 7, btnDiv, 180, 60 * 2 - 10
  stdcall RegisterButton, [window], 8, btn7, 0, 60 * 3 - 10  
  stdcall RegisterButton, [window], 9, btn8, 60, 60 * 3 - 10  
  stdcall RegisterButton, [window], 10, btn9, 120, 60 * 3 - 10  
  stdcall RegisterButton, [window], 11, btnMul, 180, 60 * 3 - 10 
  stdcall RegisterButton, [window], 12, btn4, 0, 60 * 4 - 10  
  stdcall RegisterButton, [window], 13, btn5, 60, 60 * 4 - 10  
  stdcall RegisterButton, [window], 14, btn6, 120, 60 * 4 - 10  
  stdcall RegisterButton, [window], 15, btnMin, 180, 60 * 4 - 10
  stdcall RegisterButton, [window], 16, btn1, 0, 60 * 5 - 10  
  stdcall RegisterButton, [window], 17, btn2, 60, 60 * 5 - 10  
  stdcall RegisterButton, [window], 18, btn3, 120, 60 * 5 - 10  
  stdcall RegisterButton, [window], 19, btnSum, 180, 60 * 5 - 10 
  stdcall RegisterButton, [window], 20, btnUn, 0, 60 * 6 - 10  
  stdcall RegisterButton, [window], 21, btn0, 60, 60 * 6 - 10  
  stdcall RegisterButton, [window], 22, btnDot, 120, 60 * 6 - 10  
  stdcall RegisterButton, [window], 23, btnEq, 180, 60 * 6 - 10
  jmp .finish
      
.wmcommand:
  ; TODO   
  jmp .finish
  
.wmgetminmaxinfo:
  mov eax, [lParam] ; ptr to minmaxinfo struct   
  mov [eax + MINMAXINFO.ptMinTrackSize + POINT.x], 260
  mov [eax + MINMAXINFO.ptMinTrackSize + POINT.y], 453 
  mov [eax + MINMAXINFO.ptMaxTrackSize + POINT.x], 260
  mov [eax + MINMAXINFO.ptMaxTrackSize + POINT.y], 453
 
  jmp .finish

.wmclose:  
  invoke DestroyWindow, [window]
  jmp .finish
  
.wmdestroy:
  invoke PostQuitMessage, 0
  xor eax, eax
  
.finish:
  ret
endp
     
proc RegisterButton, window, id, testEx, gridX, gridY   
  invoke CreateWindowEx, WS_EX_CLIENTEDGE, btnClassName, [testEx], WS_TABSTOP + WS_VISIBLE + WS_CHILD + BS_DEFPUSHBUTTON, [gridX], [gridY], 60, 60, [window], [id], NULL, NULL  
  ret
endp

section '.data' data readable writeable

  className TCHAR 'HummelCalculator', 0
  windowTitle TCHAR 'WinAPI', 0        
  btnClassName TCHAR 'BUTTON', 0 
     
  btnPi TCHAR 'PI', 0      
  btnE TCHAR 'E', 0   
  btnC TCHAR 'C', 0
  btnFact TCHAR 'x!', 0
  btnInv TCHAR '1/x', 0
  btnSq TCHAR 'x^2', 0
  btnSqrt TCHAR 'sqrt(x)', 0
  btnDiv TCHAR '/', 0
  btnMul TCHAR '*', 0
  btnMin TCHAR '-', 0
  btnSum TCHAR '+', 0
  btnUn TCHAR '-', 0
  btnDot TCHAR '.', 0
  btnEq TCHAR '=', 0
  btn0 TCHAR '0', 0
  btn1 TCHAR '1', 0
  btn2 TCHAR '2', 0
  btn3 TCHAR '3', 0
  btn4 TCHAR '4', 0
  btn5 TCHAR '5', 0
  btn6 TCHAR '6', 0
  btn7 TCHAR '7', 0
  btn8 TCHAR '8', 0
  btn9 TCHAR '9', 0
  
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