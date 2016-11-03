#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test005 {
		struct __A;
		struct __A_VT;

		struct __B;
		struct __B_VT;


		typedef __A* A;
		typedef __B* B;

		struct __A {
			__A_VT* __vptr;
			__A();

			static Class __class();

			static String toString(A);
		};

		struct __A_VT {
			Class __isa;

			bool (*equals)(A, Object);
			Class (*getClass)(A);
			int32_t (*hashCode)(A);
			String (*toString)(A);

			__A_VT()
			: __isa(__A::__class()),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			toString(&__A::toString)
			{}
		};

		struct __B {
			__B_VT* __vptr;
			__B();

			__A parent;

			static Class __class();

			static String toString(B);
		};

		struct __B_VT {
			Class __isa;

			bool (*equals)(B, Object);
			Class (*getClass)(B);
			int32_t (*hashCode)(B);
			String (*toString)(B);

			__B_VT()
			: __isa(__B::__class()),
			equals((bool(*)(B, Object))&__Object::equals),
			getClass((Class(*)(B))&__Object::getClass),
			hashCode((int32_t(*)(B))&__Object::hashCode),
			toString(&__B::toString)
			{}
		};
	}
}
