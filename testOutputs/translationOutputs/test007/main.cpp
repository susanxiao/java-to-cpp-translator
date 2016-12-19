#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test007;
int main(void) {
	B b = new __B();

	cout << b->parent.a << endl;
	cout << b->b << endl;
	return 0;
}