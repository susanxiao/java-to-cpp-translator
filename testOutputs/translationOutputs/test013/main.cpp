#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test013;
int main(void) {
	A a = new __A();

	A other = a;

	other->__vptr->methodSetA(other, new __String("A"));
	a->__vptr->methodPrintOther(a, other);
	return 0;
}