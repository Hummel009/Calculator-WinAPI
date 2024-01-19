;===============================================================================
;       float to ASCII conversion procedure
;
;       input:
;       buffer - pointer to memory where output will be saved
;       precision - number of digits after dot
;
;       output:
;       no immediate output
;
;       notes:
;       separate integer and mantisa part with dot '.'
;       so GOOD   123.456
;          WRONG  123,456
;
;       coded by Reverend // HTB + RAG
;===============================================================================
proc    ftoa                    buffer, precision
locals
  status_original             dw ?
  status_changed              dw ?
  integer                     dd ?
  mantisa                     dd ?
  signed                      dd ?
endl
        push    eax edi ecx
;       --------------------------------
;       set correct precision
        mov     eax, [precision]
        cmp     eax, 51
        jb      @F
        mov     eax, 51
    @@:
        mov     [precision], eax
;       --------------------------------
;       change control wortd of fpu to prevent rounding
        fnstcw  [status_original]
        mov     ax, [status_original]
        or      ax, 0000110000000000b
        mov     [status_changed], ax
        fldcw   [status_changed]
;       --------------------------------
;       check if signed
        xor     eax, eax
        fst     [signed]
        test    [signed], 80000000h
        setnz   al
        mov     [signed], eax
;       --------------------------------
;       cut integer and mantisa separately
        fld     st0
        fld     st0                     ; st0 = x, st1 = x
        frndint
        fist    [integer]               ; st0 = x, st1 = x
        fabs
        fsubp   st1, st0                ; st0 = mantisa(x)
;       --------------------------------
;       save integer part in buffer
        mov     edi, [buffer]
        push    [signed]
        push    edi
        push    10
        push    [integer]
        call    itoa
        add     edi, eax
        mov     al, '.'
        stosb
;       --------------------------------
;       save mantisa part in buffer
        mov     ecx, [precision]
        dec     ecx
    .loop:
        fimul   [ftoa.ten]
        fld     st0
        frndint
        fist    [mantisa]
        fsubp   st1, st0
        push    0
        push    edi
        push    10
        push    [mantisa]
        call    itoa
        add     edi, eax
        ftst
        fnstsw  ax
        test    ax, 0100000000000000b
        jz      @F
        test    ax, 0000010100000000b
        jz      .finish
    @@:
        loop    .loop
        fldcw   [status_original]
        fimul   [ftoa.ten]
        fist    [mantisa]
        push    0
        push    edi
        push    10
        push    [mantisa]
        call    itoa
;       --------------------------------
;       restore previous values
    .finish:
        fstp    st0
        stc
        pop     ecx edi eax
        ret
endp