#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test014 {
		void __A::methodPrintOther(A __this, A other) {
			__rt::checkNotNull(other);
			__rt::checkNotNull(__this);
			cout << other->__vptr->toString(other) << endl;
		};

		__A::__A() : __vptr(&__vtable),
			some((A)__rt::null())
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test014.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

