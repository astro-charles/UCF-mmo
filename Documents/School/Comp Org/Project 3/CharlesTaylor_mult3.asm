# NxN arrays

# for (int i=0;i<N;i++) { 
#	for (int j=0;j<N;j++) { 
#		temp = 0; 
#		for (int k=0;k<N;k++) { 
#			temp = temp + A[i][k]*B[k][j]; 
#		} 
#		C[i][j] = temp; 
#	} 
# } 
.data
.align 2
space: .asciiz " "
CR: .asciiz "\n"
multi: .asciiz " * "

# to change the size to 8x8 add more into A, B, C
A: .word 1, 2, 3, 1, 2, 3, 1, 2, 3
B: .word 4, 5, 6, 4, 5, 6, 4, 5, 6
C: .word 0 : 9


.text
# change this to the new N
li $s0, 3 # $s0 = N_Max

move $t0, $zero # $t0 = i
move $t1, $zero # $t1 = j
move $t2, $zero # $t1 = k
move $t3, $zero # $t3 = temp
loop1:
	beq $t0, $s0, endLoop1
	
	loop2:
		beq $t1, $s0, endLoop2
		loop3:
			beq $t2, $s0, endLoop3

			# A[row][column]
			# temp = temp + A[i][k]*B[k][j]; 
			# offset = row*#COLUMNS + column
			
			# A[i][k] 
			# offset = i*#COL + k = ($t0 * #3) + $t2
			
			mul $s3 ,$t0, $s0 
			add $s3, $s3, $t2
		  	sll $s3, $s3, 2
		  	
		  	lw $t4, A($s3)
			
			#B[k][j]
			# offset = k*#COL + j = ($t2 * $s0) + $t1
			
			mul $s3, $t2, $s0  
			add $s3, $s3, $t1
			sll $s3, $s3, 2
			
			lw $t5, B($s3)
			
			#temp = temp + A[i][k]*B[k][j]; 
			mul $t6, $t5, $t4  # $t6 = (A[i][k]*B[k][j]
			add $t3, $t3, $t6  # $t3 += $t6
			
			addi $t2, $t2, 1
			j loop3
		endLoop3:
		
		# C[i][j] 
		# offset = i*#COL + j = ($t0 * $s0) + $t1
		mul $s3, $t0, $s0  
		add $s3, $s3, $t1
		sll $s3, $s3, 2
		
		#c[i][j] = temp
		sw $t3, C($s3)
		
		addi $t1,$t1,1
		add  $t2,$zero,$zero
		add  $t3,$zero,$zero   #temp = 0
		j loop2
		
	endLoop2:
	addi $t0,$t0,1
	add  $t1,$zero,$zero
	j loop1
endLoop1:

li $v0, 10
syscall
