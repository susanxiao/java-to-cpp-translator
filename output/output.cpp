#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test017 {
		__A::__A(int32_t x) : __vptr(&__vtable) {
			self = this;
		};

		A __A::self(A __this) {
			return __this->self;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test017.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}
