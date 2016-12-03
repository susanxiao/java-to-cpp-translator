#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test025 {
		__A::__A(int32_t i) : __vptr(&__vtable) ,
				i(i) {
		};

		int32_t __A::methodGet(A __this) {
			return __this->i;
		};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test025.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

		__B::__B(int32_t i) : __vptr(&__vtable) , parent(i) {
		};

		int32_t __B::methodGet(B __this) {
			return 10-__this->parent.i;
		};

		Class __B::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test025.B"),__A::__class());
			return k;
		};

		__B_VT __B::__vtable;

	}
}

namespace __rt {
	template<>
	java::lang::Class Array<inputs::test025::A>::__class() {
		static java::lang::Class k =
			new java::lang::__Class(literal("[java.lang.A;"),
					java::lang::__Object::__class(),
					inputs::test025::__A::__class());
		return k;
	}
}