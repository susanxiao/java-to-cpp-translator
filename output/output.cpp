#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test014 {
		void __A::printOther(A __this, A other) {
			cout << other->__vptr->toString(other)->data << endl;
		};

		__A::__A() : __vptr(&__vtable) {};


		String __A::toString(A __this){
			Class k = __this->__vptr->getClass(__this);
			std::ostringstream sout;
			sout << k->__vptr->getName(k)->data
				 << '@' << std::hex << (uintptr_t) __this;
			return new __String(sout.str());
		}

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test014.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}
