
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test017;

int main(void)
{

	A a = new __A();
	cout << a->self()->__vptr->toString()->data << endl;

	return 0;
}

//------------------

