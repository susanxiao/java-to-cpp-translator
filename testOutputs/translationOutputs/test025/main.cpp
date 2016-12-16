
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test025;

int main(void)
{

	if (10 < 0) throw java::lang::NegativeArraySizeException();
	__rt::Array<Object>* as = (__rt::Array<Object>*) new __rt::Array<A>(10);

	for (int32_t i = 0; i < as->length; i++) {
		if (i < 0 || as->length <= i) throw java::lang::ArrayIndexOutOfBoundsException();
					checkStore(as, new __B(i));
		as->__data[i] = (Object) new __B(i);
	}
	if (10 < 0) throw java::lang::NegativeArraySizeException();
	int k = 0;

	while (k < 10) {
		if (k < 0 || as->length <= k) throw java::lang::ArrayIndexOutOfBoundsException();
					cout << ((A) as->__data[k])->__vptr->methodGet((A) as->__data[k]) << endl;
			k = k + 1;
	}
	return 0;
}

//------------------

