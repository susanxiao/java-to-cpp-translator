#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test013 {
		void __A::methodSetA(A __this, String x) {
			__rt::checkNotNull(__this);
			__rt::checkNotNull(x);
			__this->a = x;
		};

		void __A::methodPrintOther(A __this, A other) {
			__rt::checkNotNull(other);
			__rt::checkNotNull(__this);
			cout << other->__vptr->toString(other) << endl;
		};

		__A::__A() : __vptr(&__vtable),
			a((String)__rt::null())
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test013.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

