#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test032 {
		int32_t __A::methodM(A __this, int i) {
			__rt::checkNotNull(i);
			cout << ;
			return i;
		};

		void __A::methodM(A __this, A a) {
			__rt::checkNotNull(a);
			cout << ;
		};

		void __A::methodM(A __this, double d) {
			__rt::checkNotNull(d);
			cout << ;
		};

		void __A::methodM(A __this, Object o) {
			__rt::checkNotNull(o);
			cout << ;
		};

		void __A::methodM(A __this, Object o1, Object o2) {
			__rt::checkNotNull(o1);
			__rt::checkNotNull(o2);
			cout << ;
		};

		void __A::methodM(A __this, A a1, Object o2) {
			__rt::checkNotNull(a1);
			__rt::checkNotNull(o2);
			cout << ;
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

