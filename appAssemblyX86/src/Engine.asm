; READY
proc ConvertFirstAtoI
  mov ecx, 1
  mov edx, ptrData0
  mov ebx, radixes
@@:
  stdcall atoi, [edx], [ebx]
  add edx, 4
  add ebx, 4
  loop @B
  mov [intData0], eax
  
  ret
endp

; READY
proc ConvertSecondAtoI  
  mov ecx, 1
  mov edx, ptrData2
  mov ebx, radixes
@@:
  stdcall atoi, [edx], [ebx]
  add edx, 4
  add ebx, 4
  loop @B
  mov [intData2], eax
    
  ret
endp

; READY
proc ConvertResItoA  
  mov ecx, 1
  mov edx, intRes
  mov ebx, radixes
@@:
  stdcall itoa, [edx], [ebx], buffer, FALSE
  add edx, 4
  add ebx, 4
  loop @B
   
  ret       
endp