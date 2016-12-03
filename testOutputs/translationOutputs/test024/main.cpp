
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test024;

int main(void)
{

	__rt::Array<Object>* as = (__rt::Array<Object>*) new __rt::Array<A>(10);

	for (int32_t i = 0; i < as->length; i++) {
		//check array Types
		as->__data[i] = (Object) new __A(i);
	}
	int k = 0;

	while (k < 10) {
		cout << ((A) as->__data[k])->__vptr->methodGet((A) as->__data[k]) << endl;
		k = k + 1;
	}
	return 0;
}

//------------------

