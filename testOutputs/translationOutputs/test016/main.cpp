
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test016;

int main(void)
{

	A a = new __A();

	B other = new __B();

	other->some = (B) a;

	a->__vptr->printOther(a, other);

	return 0;
}

//------------------

