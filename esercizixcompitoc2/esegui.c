#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

int main(int argc, char* argv[]) {
printf("[%d]: Hello world\n",getpid());
return 0;
}
