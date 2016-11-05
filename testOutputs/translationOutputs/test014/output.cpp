#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test014 {
		void __A::printOther(A __this, A other) {
			cout << other->__vptr->toString(other)->data << endl;
		};

		__A::__A() : __vptr(&__vtable) {};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("class inputs.javalang.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}
