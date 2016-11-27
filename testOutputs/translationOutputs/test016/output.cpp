#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test016 {
		void __A::methodPrintOther(A __this, A other) {
		};

		__A::__A() : __vptr(&__vtable) {};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test016.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		void __B::methodPrintOther(B __this, A other) {
		};

		String __B::toString(B __this) {
			return __this->some->__vptr->toString(__this->some);
		};

		__B::__B() : __vptr(&__vtable) {};

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test016.B"), (Class) __rt::null());
			return k;
		};

		__B_VT __B::__vtable;

	}
}
