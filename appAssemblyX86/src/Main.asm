format PE GUI 4.0
entry start

include 'win32a.inc'

section '.text' code readable executable

start:
  invoke RegisterClass, wc                            
 
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
     
; loop 1 
cycle:
  invoke GetMessage, msg, NULL, 0, 0
  cmp eax, 0
  je exit
  invoke TranslateMessage, msg
  invoke DispatchMessage, msg
  jmp cycle 
; end loop 

exit:
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
  
  stdcall RegisterButton, [window], 0, buttonP, 0, 60 * 1 - 10  
  stdcall RegisterButton, [window], 1, buttonE, 60, 60 * 1 - 10  
  stdcall RegisterButton, [window], 2, buttonC, 120, 60 * 1 - 10  
  stdcall RegisterButton, [window], 3, buttonFactorial, 180, 60 * 1 - 10
  stdcall RegisterButton, [window], 4, buttonInverse, 0, 60 * 2 - 10  
  stdcall RegisterButton, [window], 5, buttonSquare, 60, 60 * 2 - 10  
  stdcall RegisterButton, [window], 6, buttonSquareRoot, 120, 60 * 2 - 10  
  stdcall RegisterButton, [window], 7, buttonDivide, 180, 60 * 2 - 10
  stdcall RegisterButton, [window], 8, button7, 0, 60 * 3 - 10  
  stdcall RegisterButton, [window], 9, button8, 60, 60 * 3 - 10  
  stdcall RegisterButton, [window], 10, button9, 120, 60 * 3 - 10  
  stdcall RegisterButton, [window], 11, buttonMultiple, 180, 60 * 3 - 10 
  stdcall RegisterButton, [window], 12, button4, 0, 60 * 4 - 10  
  stdcall RegisterButton, [window], 13, button5, 60, 60 * 4 - 10  
  stdcall RegisterButton, [window], 14, button6, 120, 60 * 4 - 10  
  stdcall RegisterButton, [window], 15, buttonMinus, 180, 60 * 4 - 10
  stdcall RegisterButton, [window], 16, button1, 0, 60 * 5 - 10  
  stdcall RegisterButton, [window], 17, button2, 60, 60 * 5 - 10  
  stdcall RegisterButton, [window], 18, button3, 120, 60 * 5 - 10  
  stdcall RegisterButton, [window], 19, buttonPlus, 180, 60 * 5 - 10 
  stdcall RegisterButton, [window], 20, buttonUnaryMinus, 0, 60 * 6 - 10  
  stdcall RegisterButton, [window], 21, button0, 60, 60 * 6 - 10  
  stdcall RegisterButton, [window], 22, buttonDot, 120, 60 * 6 - 10  
  stdcall RegisterButton, [window], 23, buttonEquals, 180, 60 * 6 - 10
  jmp .finish

.wmcommand:
  mov ax, word[wParam]
  mov [buttonId], ax
  
  cmp [buttonId], 0 ; pi
  jne @F
  ; TODO
@@:
  cmp [buttonId], 1 ; euler
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 2 ; clear
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 3 ; factorial
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 4 ; inverse
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 5 ; square
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 6 ; square root
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 7 ; divide
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 8 ; num 7
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 9 ; num 8
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 10 ; num 9
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 11 ; multiple
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 12 ; num 4
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 13 ; num 5
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 14 ; num 6
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 15 ; minus
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 16 ; num 1
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 17 ; num 2
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 18 ; num 3
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 19 ; plus
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 20 ; unary minus
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 21 ; num 0
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 22 ; dot
  jne @F 
  ; TODO
@@:
  cmp [buttonId], 23 ; equals
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

  className        TCHAR 'HummelCalculator', 0
  windowTitle      TCHAR 'WinAPI', 0
  buttonClassName  TCHAR 'BUTTON', 0      
  fieldClassName   TCHAR 'STATIC', 0      
  fieldText        TCHAR '', 0 

  buttonP          TCHAR 'P', 0      
  buttonE          TCHAR 'E', 0 
  
  buttonC          TCHAR 'C', 0
  
  buttonDot        TCHAR '.', 0 
  buttonUnaryMinus TCHAR '-', 0
  
  buttonFactorial  TCHAR 'x!', 0
  buttonInverse    TCHAR '1/x', 0
  buttonSquare     TCHAR 'x^2', 0
  buttonSquareRoot TCHAR 'sqrt(x)', 0
  
  buttonDivide     TCHAR '/', 0
  buttonMultiple   TCHAR '*', 0
  buttonMinus      TCHAR '-', 0
  buttonPlus       TCHAR '+', 0
  
  buttonEquals     TCHAR '=', 0
  
  button0          TCHAR '0', 0
  button1          TCHAR '1', 0
  button2          TCHAR '2', 0
  button3          TCHAR '3', 0
  button4          TCHAR '4', 0
  button5          TCHAR '5', 0
  button6          TCHAR '6', 0
  button7          TCHAR '7', 0
  button8          TCHAR '8', 0
  button9          TCHAR '9', 0
  
  screenWidth      dd 0
  screenHeight     dd 0
  windowWidth      dd 260
  windowHeight     dd 453
  windowX          dd 0
  windowY          dd 0
  
  field    dd 0
  buttonId dw 0

  wc  WNDCLASS 0, WindowProc, 0, 0, NULL, NULL, NULL, COLOR_WINDOW + 1, NULL, className
  msg MSG

section '.idata' import data readable writeable

  library kernel32, 'KERNEL32.DLL', user32, 'USER32.DLL'

  include 'api\kernel32.inc'
  include 'api\user32.inc'