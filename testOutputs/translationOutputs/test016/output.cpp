#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test016 {
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

		void __B::printOther(B __this, A other) {
			cout << other->__vptr->toString(other)->data << endl;
		};

		String __B::toString(B __this) {
		};

		__B::__B() : __vptr(&__vtable) {};

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("class inputs.javalang.B"), (Class) __rt::null());
			return k;
		};

		__B_VT __B::__vtable;

	}
}
