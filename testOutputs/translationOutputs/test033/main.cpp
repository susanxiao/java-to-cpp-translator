#include <iostream>
#include <sstream>

#include "java_lang.h"
#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test033;
int main(void) {
	A a = new __A();

	uint8_t b = 1;

	__A::methodMInt((int32_t) b);
	__A::methodMA(a);
	__A::methodMDouble(1.0);
	__A::methodMObject((Object) a);
	__A::methodMAObject(new __A(), (Object) a);
	__A::methodMObjectObject(new __Object(), (Object) a);
	return 0;
}