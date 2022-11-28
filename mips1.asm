.data # variable declarations follow this line # 数据变量声明
    array:
        .space 4000
    words:
    	.word 1, 2

.text # instructions follow this line

getNum:
    li $v0 5
    syscall
    mul $t0 $s2 4
    sw $v0 array($t0)
    addi $s2 $s2 1
    bne $s0 $s2 getNum
    jr $ra
   
printNum:
    mul $t0 $s3 4 
    lw $a0 array($t0)
    li $v0 1
    syscall
    addi $s3 $s3 1
    bne $s0 $s3 printNum
    jr $ra

main: # indicates start of code (first instruction to execute)
    li $v0 5
    syscall 
    move $s0 $v0
    addi $s2 $zero 0
    jal getNum
    addi $s3 $zero 0
    jal printNum
    li $v0 10
    syscall