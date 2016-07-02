/*Usage:
./generator nomeFileDaGenerare generate 
genera un file binario con triplette di numeri interi A B C. 
A e B sono numeri casuali (anche negativi) 
C è scelto casualmente tra A e B.

./generator nomeFileDaVisualizzare print
mostra a video il contenuto del file binario nomeFileDaVisualizzare

*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <signal.h>
#include <fcntl.h>
#include <unistd.h>
#include <time.h>

#define NUM_TRIPLES 5

void generate_input(char *fileName);
void print_input(char *fileName);
int generate_random();

int main(int argc, char **argv){
	if(argc!=3){
		fprintf(stderr, "Invalid number of arguments.\n");
        fprintf(stderr, "Usage:\n\t%s file generate|print\n", argv[0]);
		exit(1);	
	}//end if	
	else if(strcmp(argv[2],"generate")==0){
		generate_input(argv[1]);
	}//end else if generate
	else if(strcmp(argv[2],"print")==0){
		print_input(argv[1]);
	}//end else if print
	else{
		fprintf(stderr, "Invalid argument: second argument must be one of generate or print\n");
		exit(1);	
	}//end else
	
    return 0;
}//end main

void generate_input(char *fileName){
	unsigned int seed;
	int fd1,i;
	seed=getpid()*time(NULL);
	srand(seed);
	fd1=creat(fileName,00640);
	if(fd1<0){
		perror("Creat Error");
		exit(1);	
	}//end if
	for(i=0;i<NUM_TRIPLES;i++){
		int n,triple[3];
		triple[0]=generate_random();
		triple[1]=generate_random();
		triple[2]=((rand()%2)==0)?triple[0]:triple[1];
		n=write(fd1,triple,3*sizeof(int));
		if(n<0){
			perror("Write Error");
			exit(1);
		}//end if
	}//end for
	close(fd1);
	return;
}//end generate_input

void print_input(char *fileName){
	int fd2,val,nr;
	fd2=open(fileName,O_RDONLY);
	if(fd2<0){
		perror("Open Error");
		exit(1);
	}//end if
    printf("[ ");
	while( (nr=read(fd2, &val, sizeof(int))) > 0 ){
		printf("%d ",val);
	}//end while
    
	if(nr<0){
		perror("Read Error");
		exit(1);
	}//end if

	printf("]\n");
	close(fd2);
	return;
}//end print_input

int generate_random(){
	int limit=20,rnd,abs_val,is_negative;
	rnd=rand();
	abs_val=rnd%limit;
	rnd=rand();
	is_negative=rnd%2;//valore booleano
	if(is_negative) return -abs_val;
	else return abs_val;
}//end generate_random






