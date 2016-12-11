#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test032 {
		struct __A;
		struct __A_VT;


		typedef __A* A;

		struct __A {
			__A_VT* __vptr;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static int32_t methodM_int(A, int);
			static void methodM_A(A, A);
			static void methodM_double(A, double);
			static void methodM_Object(A, Object);
			static void methodM_ObjectObject(A, Object, Object);
			static void methodM_AObject(A, A, Object);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			void (*methodM_AObject)(A, A,Object);
			void (*methodM_ObjectObject)(A, Object,Object);
			void (*methodM_Object)(A, Object);
			void (*methodM_double)(A, double);
			void (*methodM_A)(A, A);
			int (*methodM_int)(A, int);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodM_AObject (&__A::methodM_AObject),
			methodM_ObjectObject (&__A::methodM_ObjectObject),
			methodM_Object (&__A::methodM_Object),
			methodM_double (&__A::methodM_double),
			methodM_A (&__A::methodM_A),
			methodM_int (&__A::methodM_int)
			{}
		};

	}
}

