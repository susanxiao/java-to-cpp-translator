#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test005;
int main(void) {
	B b = new __B();

	A a1 = new __A();

	A a2 = (A) b;

	cout << a1->__vptr->toString(a1) << endl;
	cout << a2->__vptr->toString(a2) << endl;
	return 0;
}