#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test041;
int main(void) {
	C c = new __C();

	c->__vptr->methodMAObject(c, new __A(), (Object) c);
	c->__vptr->methodMCC(c, c, c);
	c->__vptr->methodMObjectA(c, (Object) c, (A) c);
	return 0;
}