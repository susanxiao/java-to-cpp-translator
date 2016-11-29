#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test011 {
		void __A::methodSetA(A __this, String x) {
			__this->a = x;
		};

		void __A::methodPrintOther(A __this, A other) {
			cout << other->a << endl;
			cout << other->a << endl;
		};

		String __A::toString(A __this) {
			return __this->a;
		};

		__A::__A() : __vptr(&__vtable) {};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test011.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		__B1::__B1() : __vptr(&__vtable) {};

		Class __B1::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test011.B1"), (Class) __rt::null());
			return k;
		};

		__B1_VT __B1::__vtable;

		__B2::__B2() : __vptr(&__vtable) {};

		Class __B2::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test011.B2"), (Class) __rt::null());
			return k;
		};

		__B2_VT __B2::__vtable;

		__C::__C() : __vptr(&__vtable) {};

		Class __C::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test011.C"), (Class) __rt::null());
			return k;
		};

		__C_VT __C::__vtable;

	}
}
