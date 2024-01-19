; READY
proc ConvertFirstAtoI
  mov ebx, 0
  mov ecx, 0
  
.cycle:
  cmp [data0+ebx], '-'
  je .iterinc
  
  mov eax, dword[data0+ebx]
  mov dword[localData0+ecx], eax
  inc ecx
  
  cmp ebx, 254
  je .exit
  
.iterinc:
  inc ebx
  jmp .cycle

.exit:
  cmp ebx, ecx
  jne .next
  
  stdcall atoi, localData0, [radix] 
  mov [intData0], eax   
  neg [intData0]

  ret

.next:
  stdcall atoi, localData0, [radix]
  mov [intData0], eax 

  ret
endp

; READY
proc ConvertSecondAtoI
  mov ebx, 0
  mov ecx, 0

.cycle:
  cmp [data2+ebx], '-'
  je .iterinc
  
  mov eax, dword[data2+ebx]
  mov dword[localData2+ecx], eax
  inc ecx
  
  cmp ebx, 254
  je .exit
  
.iterinc:
  inc ebx
  jmp .cycle

.exit:
  cmp ebx, ecx
  jne .next
    
  stdcall atoi, localData2, [radix]
  mov [intData2], eax  
  neg [intData2]
  
  ret

.next:  
  stdcall atoi, localData2, [radix]
  mov [intData2], eax

  ret
endp

; READY
proc ConvertResItoA  
  stdcall itoa, [intRes], [radix], buffer, TRUE

  ret       
endp