; READY
proc ConvertFirstAtoI
  mov ecx, 1
  mov edx, ptrData0
  mov ebx, radixes

  stdcall atoi, [edx], [ebx]
  mov [intData0], eax
  
  ret
endp

; READY
proc ConvertSecondAtoI  
  mov ecx, 1
  mov edx, ptrData2
  mov ebx, radixes

  stdcall atoi, [edx], [ebx]

  mov [intData2], eax
    
  ret
endp

; READY
proc ConvertResItoA  
  mov ecx, 1
  mov edx, intRes
  mov ebx, radixes

  stdcall itoa, [edx], [ebx], buffer, TRUE

  ret       
endp