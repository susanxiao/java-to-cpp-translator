
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test027;

int main(void)
{

	__rt::Array<A>* as = new __rt::Array<A>(10);

	for (int32_t i = 0; i < as->length; i++) {
		checkIndex(as, i);
		checkStore(as, new __A(i));
		as->__data[i] = new __A(i);
	}
	int k = 0;

	while (k < 11) {
		checkIndex(as, k);
		cout << ((A) as->__data[k])->__vptr->methodGet((A) as->__data[k]) << endl;
			k = k + 1;
	}
	return 0;
}

//------------------

