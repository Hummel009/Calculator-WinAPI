format PE GUI 4.0
entry Start

include 'win32a.inc'

section '.text' code readable executable

Start:
  invoke RegisteristerClass, wc                            
 
  invoke GetSystemMetrics, SM_CXSCREEN
  mov [screenWidth], eax

  invoke GetSystemMetrics, SM_CYSCREEN
  mov [screenHeight], eax
  
  ; getting X, Y to display window in center
  mov eax, [screenWidth]
  sub eax, [windowWidth]
  sar eax, 1 ; divide on 2     
  mov [windowX], eax

  mov eax, [screenHeight]
  sub eax, [windowHeight]
  sar eax, 1 ; divide on 2
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
  
.defwindowproc:
  invoke DefWindowProc, [window], [msg], [wParam], [lParam]
  jmp .finish

.wmcreate:      
  stdcall RegisterFld, [window]
  
  stdcall RegisterButton, [window], 0, buttonPi, 0, 60 * 1 - 10  
  stdcall RegisterButton, [window], 1, buttonE, 60, 60 * 1 - 10  
  stdcall RegisterButton, [window], 2, buttonC, 120, 60 * 1 - 10  
  stdcall RegisterButton, [window], 3, buttonFact, 180, 60 * 1 - 10
  stdcall RegisterButton, [window], 4, buttonInv, 0, 60 * 2 - 10  
  stdcall RegisterButton, [window], 5, buttonSq, 60, 60 * 2 - 10  
  stdcall RegisterButton, [window], 6, buttonSqrt, 120, 60 * 2 - 10  
  stdcall RegisterButton, [window], 7, buttonDiv, 180, 60 * 2 - 10
  stdcall RegisterButton, [window], 8, button7, 0, 60 * 3 - 10  
  stdcall RegisterButton, [window], 9, button8, 60, 60 * 3 - 10  
  stdcall RegisterButton, [window], 10, button9, 120, 60 * 3 - 10  
  stdcall RegisterButton, [window], 11, buttonMul, 180, 60 * 3 - 10 
  stdcall RegisterButton, [window], 12, button4, 0, 60 * 4 - 10  
  stdcall RegisterButton, [window], 13, button5, 60, 60 * 4 - 10  
  stdcall RegisterButton, [window], 14, button6, 120, 60 * 4 - 10  
  stdcall RegisterButton, [window], 15, buttonMin, 180, 60 * 4 - 10
  stdcall RegisterButton, [window], 16, button1, 0, 60 * 5 - 10  
  stdcall RegisterButton, [window], 17, button2, 60, 60 * 5 - 10  
  stdcall RegisterButton, [window], 18, button3, 120, 60 * 5 - 10  
  stdcall RegisterButton, [window], 19, buttonSum, 180, 60 * 5 - 10 
  stdcall RegisterButton, [window], 20, buttonUn, 0, 60 * 6 - 10  
  stdcall RegisterButton, [window], 21, button0, 60, 60 * 6 - 10  
  stdcall RegisterButton, [window], 22, buttonDot, 120, 60 * 6 - 10  
  stdcall RegisterButton, [window], 23, buttonEq, 180, 60 * 6 - 10
  jmp .finish

.wmcommand:
  mov ax, word[wParam]
  mov [buttonId], ax
  
  cmp [buttonId], 0
  jne @F
  ; TODO
@@:
  cmp [buttonId], 1
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 2
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 3
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 4
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 5
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 6
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 7
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 8
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 9
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 10
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 11
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 12
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 13
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 14
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 15
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 16
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 17
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 18
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 19
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 20
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 21
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 22
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 23
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
  invoke DestroyWindow, [window]
  jmp .finish
  
.wmdestroy:
  invoke PostQuitMessage, 0
  xor eax, eax
  
.finish:
  ret
endp

proc RegisterButton, window, id, text, gridX, gridY   
  invoke CreateWindowEx, WS_EX_CLIENTEDGE, buttonClassName, [text], WS_TABSTOP + WS_VISIBLE + WS_CHILD + BS_DEFPUSHBUTTON, [gridX], [gridY], 60, 60, [window], [id], NULL, NULL  
  ret
endp

proc RegisterFld, window   
  invoke CreateWindowEx, WS_EX_CLIENTEDGE, fieldClassName, fieldText, WS_TABSTOP + WS_VISIBLE + WS_CHILD + BS_DEFPUSHBUTTON, 0, 0, 240, 50, [window], NULL, NULL, NULL 
  mov [field], eax 
  ret
endp

section '.data' data readable writeable

  className TCHAR 'HummelCalculator', 0
  windowTitle TCHAR 'WinAPI', 0        
  buttonClassName TCHAR 'BUTTON', 0      
  fieldClassName TCHAR 'STATIC', 0      
  fieldText TCHAR '', 0 

  buttonPi TCHAR 'PI', 0      
  buttonE TCHAR 'E', 0   
  buttonC TCHAR 'C', 0
  buttonFact TCHAR 'x!', 0
  buttonInv TCHAR '1/x', 0
  buttonSq TCHAR 'x^2', 0
  buttonSqrt TCHAR 'sqrt(x)', 0
  buttonDiv TCHAR '/', 0
  buttonMul TCHAR '*', 0
  buttonMin TCHAR '-', 0
  buttonSum TCHAR '+', 0
  buttonUn TCHAR '-', 0
  buttonDot TCHAR '.', 0
  buttonEq TCHAR '=', 0
  button0 TCHAR '0', 0
  button1 TCHAR '1', 0
  button2 TCHAR '2', 0
  button3 TCHAR '3', 0
  button4 TCHAR '4', 0
  button5 TCHAR '5', 0
  button6 TCHAR '6', 0
  button7 TCHAR '7', 0
  button8 TCHAR '8', 0
  button9 TCHAR '9', 0
  
  screenWidth dd 0
  screenHeight dd 0
  windowWidth dd 260
  windowHeight dd 453
  windowX dd 0
  windowY dd 0
  
  field dd 0
  buttonId dw 0

  wc WNDCLASS 0, WindowProc, 0, 0, NULL, NULL, NULL, COLOR_WINDOW + 1, NULL, className

  msg MSG

section '.idata' import data readable writeable

  library kernel32, 'KERNEL32.DLL', user32, 'USER32.DLL'

  include 'api\kernel32.inc'
  include 'api\user32.inc'