
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test031;

int main(void)
{

	__rt::Array<int>* as = new __rt::Array<int>(5);

	for (int32_t i = 0; i < as->length; i++) {
		for (int32_t j = 0; j < as->__data[i]->length; j++) {
			 = i * j;
	}
	}
	for (int32_t i = 0; i < as->length; i++) {
		for (int32_t j = 0; j < as->__data[i]->length; j++) {
			cout << as->__data[i] << endl;
	}
	}
	return 0;
}

//------------------


