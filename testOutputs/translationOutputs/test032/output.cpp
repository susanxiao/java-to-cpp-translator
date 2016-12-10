#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test032 {
		int32_t __A::methodM_int(A __this, int i) {
			cout << "A.m(int)" <<endl;
			return i;
		};

		void __A::methodM_A(A __this, A a) {
			__rt::checkNotNull(a);
			cout << "A.m(A)" <<endl;
		};

		void __A::methodM_double(A __this, double d) {
			cout << "A.m(double)" <<endl;
		};

		void __A::methodM_Object(A __this, Object o) {
			__rt::checkNotNull(o);
			cout << "A.m(Object)" <<endl;
		};

		void __A::methodM_ObjectObject(A __this, Object o1, Object o2) {
			__rt::checkNotNull(o1);
			__rt::checkNotNull(o2);
			cout << "A.m(Object, Object)" <<endl;
		};

		void __A::methodM_AObject(A __this, A a1, Object o2) {
			__rt::checkNotNull(a1);
			__rt::checkNotNull(o2);
			cout << "A.m(A, Object)" <<endl;
		};

		__A::__A() : __vptr(&__vtable)
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test032.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

