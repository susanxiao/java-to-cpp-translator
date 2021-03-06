#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test030 {
		__A::__A(int32_t i) : __vptr(&__vtable) ,
				i(i) {
		};

		int32_t __A::methodGet(A __this) {
			__rt::checkNotNull(__this);
			return __this->i;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test030.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}

namespace __rt {
	template<>
	java::lang::Class Array<inputs::test030::__A>::__class() {
		static java::lang::Class k =
			new java::lang::__Class(literal("[Linputs.test030.A;"),
					java::lang::__Object::__class(),
					inputs::test030::__A::__class());
		return k;
	}
	template<>
	java::lang::Class Array< __rt::Array<inputs::test030::__A>* >::__class() {
		static java::lang::Class k =
			new java::lang::__Class(literal("[[Linputs.test030.A;"),
					java::lang::__Object::__class(),
					__rt::Array<inputs::test030::__A>::__class());
		return k;
	}
}