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
  
  ;getting X, Y to display wnd in center
  mov eax, [screenWidth]
	sub eax, [wndWidth]
	sar eax, 1 ;divide on 2     
	mov [wndX], eax

	mov eax, [screenHeight]
	sub eax, [wndHeight]
	sar eax, 1 ;divide on 2
	mov [wndY], eax

  invoke CreateWindowEx, WS_EX_CLIENTEDGE, className, wndTitle, WS_VISIBLE + WS_OVERLAPPEDWINDOW, [wndX], [wndY], [wndWidth], [wndHeight], NULL, NULL, NULL, NULL
 
msg_loop:
  invoke GetMessage, msg, NULL, 0, 0
  cmp eax, 0
  je end_loop
  invoke TranslateMessage, msg
  invoke DispatchMessage, msg
  jmp msg_loop

end_loop:
  invoke ExitProcess, [msg.wParam]

proc WindowProc uses ebx esi edi, wnd, msg, wParam, lParam
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
  invoke DefWindowProc, [wnd], [msg], [wParam], [lParam]
  jmp .finish

.wmcreate:      
  stdcall RegFld, [wnd]
  
  stdcall RegBtn, [wnd], 0, btnPi, 0, 60 * 1 - 10  
  stdcall RegBtn, [wnd], 1, btnE, 60, 60 * 1 - 10  
  stdcall RegBtn, [wnd], 2, btnC, 120, 60 * 1 - 10  
  stdcall RegBtn, [wnd], 3, btnFact, 180, 60 * 1 - 10
  stdcall RegBtn, [wnd], 4, btnInv, 0, 60 * 2 - 10  
  stdcall RegBtn, [wnd], 5, btnSq, 60, 60 * 2 - 10  
  stdcall RegBtn, [wnd], 6, btnSqrt, 120, 60 * 2 - 10  
  stdcall RegBtn, [wnd], 7, btnDiv, 180, 60 * 2 - 10
  stdcall RegBtn, [wnd], 8, btn7, 0, 60 * 3 - 10  
  stdcall RegBtn, [wnd], 9, btn8, 60, 60 * 3 - 10  
  stdcall RegBtn, [wnd], 10, btn9, 120, 60 * 3 - 10  
  stdcall RegBtn, [wnd], 11, btnMul, 180, 60 * 3 - 10 
  stdcall RegBtn, [wnd], 12, btn4, 0, 60 * 4 - 10  
  stdcall RegBtn, [wnd], 13, btn5, 60, 60 * 4 - 10  
  stdcall RegBtn, [wnd], 14, btn6, 120, 60 * 4 - 10  
  stdcall RegBtn, [wnd], 15, btnMin, 180, 60 * 4 - 10
  stdcall RegBtn, [wnd], 16, btn1, 0, 60 * 5 - 10  
  stdcall RegBtn, [wnd], 17, btn2, 60, 60 * 5 - 10  
  stdcall RegBtn, [wnd], 18, btn3, 120, 60 * 5 - 10  
  stdcall RegBtn, [wnd], 19, btnSum, 180, 60 * 5 - 10 
  stdcall RegBtn, [wnd], 20, btnUn, 0, 60 * 6 - 10  
  stdcall RegBtn, [wnd], 21, btn0, 60, 60 * 6 - 10  
  stdcall RegBtn, [wnd], 22, btnDot, 120, 60 * 6 - 10  
  stdcall RegBtn, [wnd], 23, btnEq, 180, 60 * 6 - 10
  jmp .finish
      
.wmcommand:
  mov ax, word[wParam]
  mov [btnId], ax
  
  cmp [btnId], 0
  jne @F
  ; TODO
@@:
  cmp [btnId], 1
  jne @F 
  ; TODO
@@:
  cmp [btnId], 2
  jne @F 
  ; TODO
@@:
  cmp [btnId], 3
  jne @F 
  ; TODO
@@:
  cmp [btnId], 4
  jne @F 
  ; TODO
@@:
  cmp [btnId], 5
  jne @F 
  ; TODO
@@:
  cmp [btnId], 6
  jne @F 
  ; TODO
@@:
  cmp [btnId], 7
  jne @F 
  ; TODO
@@:
  cmp [btnId], 8
  jne @F 
  ; TODO
@@:
  cmp [btnId], 9
  jne @F 
  ; TODO
@@:
  cmp [btnId], 10
  jne @F 
  ; TODO
@@:
  cmp [btnId], 11
  jne @F 
  ; TODO
@@:
  cmp [btnId], 12
  jne @F 
  ; TODO
@@:
  cmp [btnId], 13
  jne @F 
  ; TODO
@@:
  cmp [btnId], 14
  jne @F 
  ; TODO
@@:
  cmp [btnId], 15
  jne @F 
  ; TODO
@@:
  cmp [btnId], 16
  jne @F 
  ; TODO
@@:
  cmp [btnId], 17
  jne @F 
  ; TODO
@@:
  cmp [btnId], 18
  jne @F 
  ; TODO
@@:
  cmp [btnId], 19
  jne @F 
  ; TODO
@@:
  cmp [btnId], 20
  jne @F 
  ; TODO
@@:
  cmp [btnId], 21
  jne @F 
  ; TODO
@@:
  cmp [btnId], 22
  jne @F 
  ; TODO
@@:
  cmp [btnId], 23
  jne @F 
  ; TODO
@@:   
  jmp .finish
  
.wmgetminmaxinfo:
  mov eax, [lParam] ; ptr to minmaxinfo struct   
  mov [eax + MINMAXINFO.ptMinTrackSize + POINT.x], 260
  mov [eax + MINMAXINFO.ptMinTrackSize + POINT.y], 453 
  mov [eax + MINMAXINFO.ptMaxTrackSize + POINT.x], 260
  mov [eax + MINMAXINFO.ptMaxTrackSize + POINT.y], 453
 
  jmp .finish

.wmclose:  
  invoke DestroyWindow, [wnd]
  jmp .finish
  
.wmdestroy:
  invoke PostQuitMessage, 0
  xor eax, eax
  
.finish:
  ret
endp
     
proc RegBtn, wnd, id, text, gridX, gridY   
  invoke CreateWindowEx, WS_EX_CLIENTEDGE, btnClassName, [text], WS_TABSTOP + WS_VISIBLE + WS_CHILD + BS_DEFPUSHBUTTON, [gridX], [gridY], 60, 60, [wnd], [id], NULL, NULL  
  ret
endp

proc RegFld, wnd   
  invoke CreateWindowEx, WS_EX_CLIENTEDGE, fldClassName, fldText, WS_TABSTOP + WS_VISIBLE + WS_CHILD + BS_DEFPUSHBUTTON, 0, 0, 240, 50, [wnd], NULL, NULL, NULL 
  mov [field], eax 
  ret
endp

section '.data' data readable writeable

  className TCHAR 'HummelCalculator', 0
  wndTitle TCHAR 'WinAPI', 0        
  btnClassName TCHAR 'BUTTON', 0      
  fldClassName TCHAR 'STATIC', 0      
  fldText TCHAR '', 0 

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
  wndWidth dd 260
  wndHeight dd 453
  wndX dd 0
  wndY dd 0
  
  field dd 0
  btnId dw 0

  wc WNDCLASS 0, WindowProc, 0, 0, NULL, NULL, NULL, COLOR_WINDOW + 1, NULL, className

  msg MSG

section '.idata' import data readable writeable

  library kernel32, 'KERNEL32.DLL', user32, 'USER32.DLL'

  include 'api\kernel32.inc'
  include 'api\user32.inc'