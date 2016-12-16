
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

	if (5 < 0) throw java::lang::NegativeArraySizeException();
	__rt::Array<__rt::Array<int>*>* as = new __rt::Array<__rt::Array<int>*>(5);

	for (int32_t i = 0; i < as->length; i++) {
		if (i < 0 || as->length <= i) throw java::lang::ArrayIndexOutOfBoundsException();
		as->__data[i] = new __rt::Array<int>(5);
				for (int32_t j = 0; j < as->__data[i]->length; j++) {
		if (j < 0 || as->__data[i]->length <= j) throw java::lang::ArrayIndexOutOfBoundsException();
					as->__data[i]->__data[j] = i * j;
	}
	}
	for (int32_t i = 0; i < as->length; i++) {
		if (i < 0 || as->length <= i) throw java::lang::ArrayIndexOutOfBoundsException();
				for (int32_t j = 0; j < as->__data[i]->length; j++) {
		if (j < 0 || as->__data[i]->length <= j) throw java::lang::ArrayIndexOutOfBoundsException();
					cout << as->__data[i]->__data[j] << endl;
	}
	}
	return 0;
}

//------------------

