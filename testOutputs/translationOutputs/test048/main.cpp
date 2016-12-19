#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test048;
int main(void) {
	B b = new __B();

	A a = new __A();

	a->__vptr->methodMA(a, (A) b)->__vptr->methodMA(new __A(), (A) b)->__vptr->methodM(new __A());
	return 0;
}