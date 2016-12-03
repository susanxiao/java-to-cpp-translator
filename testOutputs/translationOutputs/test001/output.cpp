#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test001 {
		String __A::toString(A __this) {
			return new __String("A");
		};

		__A::__A() : __vptr(&__vtable)
		{};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test001.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

