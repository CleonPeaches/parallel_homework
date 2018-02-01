#include "stdafx.h"
#include <omp.h>
#include <iostream>
#include <chrono>
#include <fstream>

using namespace std;
using namespace std::chrono;

long seqSum = 0;
long parSum = 0;

int computeFactorial(int num) {
	int out = 1;
	for (int i = 1; i <= num; i++) {
		out = out * i;
	}
	return out;
}

int main()
{
	//change this to adjust problem size
	const int size = 160001782;

	//change this to adjust the number of threads
	int threads = 16;
	
	omp_set_num_threads(threads);

	int * array;
	array = new int[size];

	int * array2;
	array2 = new int[size];

	for (int i = 0; i < size; i++) {
		array[i] = i % 6;
	}

	for (int i = 0; i < size; i++) {
		array2[i] = i % 6;
	}


	//start sequential timer
	
	double t1 = clock();

	for (int i = 0; i < size; i++) {
		array[i] = computeFactorial(array[i]);
		seqSum += array[i];
	}

	//stop timer
	t1 = clock() - t1;

	//start parallel timer
	double t2 = clock();


	#pragma omp parallel for reduction(+: parSum)
		for (int i = 0; i < size; i++) {
			array2[i] = computeFactorial(array2[i]);
			parSum += array2[i];
		}

		//stop timer
		t2 = clock() - t2;
		double dif1 = (double)t1 / (double)CLOCKS_PER_SEC;
		double dif2 = (double)t2 / (double)CLOCKS_PER_SEC;
		
		std::ofstream outfile;

		outfile.open("out.txt", std::ios_base::app);
		outfile << size << ", " << threads << ", " << dif1 << ", " << dif2 << endl;
		printf("Sequential time is %lf milliseconds.\n", dif1);
		printf("Parallel time is %lf milliseconds.\n", dif2);

		cout << "Sum is " << seqSum << ", " << parSum << endl;
    return 0;
}


