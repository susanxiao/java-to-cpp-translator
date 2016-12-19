#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test047;
int main(void) {
	B b = new __B();

	A a = new __A();

	((B) a->__vptr->methodMA(a, (A) b))->__vptr->methodMB(new __B(), b)->__vptr->methodM(new __C());
	return 0;
}