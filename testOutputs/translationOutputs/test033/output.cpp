#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test033 {
		int32_t __A::methodMInt(int32_t i) {
			__rt::checkNotNull(__this);
			cout << "A.m(int)" << endl;
			return i;
		};

		void __A::methodMA(A a) {
			__rt::checkNotNull(a);
			__rt::checkNotNull(__this);
			cout << "A.m(A)" << endl;
		};

		void __A::methodMDouble(double d) {
			__rt::checkNotNull(__this);
			cout << "A.m(double)" << endl;
		};

		void __A::methodMObject(Object o) {
			__rt::checkNotNull(__this);
			__rt::checkNotNull(o);
			cout << "A.m(Object)" << endl;
		};

		void __A::methodMObjectObject(Object o1, Object o2) {
			__rt::checkNotNull(o1);
			__rt::checkNotNull(__this);
			__rt::checkNotNull(o2);
			cout << "A.m(Object, Object)" << endl;
		};

		void __A::methodMAObject(A a1, Object o2) {
			__rt::checkNotNull(a1);
			__rt::checkNotNull(__this);
			__rt::checkNotNull(o2);
			cout << "A.m(A, Object)" << endl;
		};

		__A::__A() : __vptr(&__vtable)
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test033.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

