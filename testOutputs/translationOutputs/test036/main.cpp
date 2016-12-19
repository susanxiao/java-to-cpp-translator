#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test036;
int main(void) {
	A a = new __A();

	a->__vptr->methodMObjectObject(a, (Object) new __A(), (Object) a);
	a->__vptr->methodMAObject(a, a, new __Object());
	a->__vptr->methodMObjectA(a, (Object) a, a);
	return 0;
}