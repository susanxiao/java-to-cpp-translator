#include "output.h"
#include <sstream>

using namespace java::lang;
using namespace std;
namespace inputs {
	namespace test013 {
		void __A::methodSetA(A __this, String x) {
			__this->a = x;
		};

		void __A::methodPrintOther(A __this, A other) {
			
			try {
				std::stringstream ss;
				ss << other;
				std::string tmp = ss.str();
				int count = 0;
				for(int i = 0; i < tmp.length(); i++){
					if(tmp[i] != '0'){ count += 1; }
				}
				if(count == 2 || count == 1){ throw java::lang::NullPointerException(); }
				Class k = other->__vptr->getClass(other);
				std::string paramClass = k->__vptr->getName(k)->data;
				Class thisK = __this->__vptr->getClass(__this);
				std::string thisClass = thisK->__vptr->getName(thisK)->data;
				//if(paramClass != thisClass){ throw java::lang::ClassCastException();}
				cout << other->__vptr->toString(other)->data << endl;
			}
			catch(const NullPointerException &ex){
				cout << "java.lang.NullPointerException" << endl;
				cout << "	at inputs.test013.A.methodPrintOther" << endl;
			}
			catch(const ClassCastException &ex){
				cout << "java.lang.ClassCastException" << endl;
				cout << "	at inputs.test013.A" << endl;
			}

		};

		__A::__A() : __vptr(&__vtable) {};

		Class __A::__class() {
			static Class k =
			new __Class(__rt::literal("inputs.test013.A"), (Class) __rt::null());
			return k;
		};

		__A_VT __A::__vtable;

	}
}
