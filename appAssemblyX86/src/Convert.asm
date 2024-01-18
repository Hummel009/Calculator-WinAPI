; READY
proc CountSymbol uses eax edi ecx, bufferLen, symbol: byte
  mov [quantity], 0 
  mov al, [symbol]  
  mov edi, buffer    
  mov ecx, [bufferLen]

; loop: find symbol
.cycle:
  repne scasb
  jnz .finish

  inc [quantity]
  jmp .cycle
; end loop

.finish:
  ret
endp