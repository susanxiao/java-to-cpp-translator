
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test005;

int main(void)
{

	B b = new __B();

	A a1 = new __A();

	A a2 = (A) b;

	cout << a1->__vptr->toString()->data << endl;

	cout << a2->__vptr->toString()->data << endl;

	return 0;
}

//------------------

