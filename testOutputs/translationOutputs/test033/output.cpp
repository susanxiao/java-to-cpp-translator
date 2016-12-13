#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test033 {
		int32_t __A::methodM(, int i) {
			cout << ;
			return i;
		};

		void __A::methodM(, A a) {
			__rt::checkNotNull(a);
			cout << ;
		};

		void __A::methodM(, double d) {
			cout << ;
		};

		void __A::methodM(, Object o) {
			__rt::checkNotNull(o);
			cout << ;
		};

		void __A::methodM(, Object o1, Object o2) {
			__rt::checkNotNull(o1);
			__rt::checkNotNull(o2);
			cout << ;
		};

		void __A::methodM(, A a1, Object o2) {
			__rt::checkNotNull(a1);
			__rt::checkNotNull(o2);
			cout << ;
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

